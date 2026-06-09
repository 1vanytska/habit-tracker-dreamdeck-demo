provider "aws" {}

terraform {
    backend "s3" {
        bucket = "dreamdeck-s3-bucket"
        key = "ecr.tfstate"
        region = "eu-west-1"
    }
}

resource "aws_ecr_repository" "ecr" {
    name = var.repo_name
    image_tag_mutability = "MUTABLE"
    image_scanning_configuration {
        scan_on_push = true
    }
}

resource "aws_ecr_lifecycle_policy" "lifecycle" {
  repository = aws_ecr_repository.ecr.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description = "Keep last ${var.image_limit} images"
        selection = {
          tagStatus = "any"
          countType = "imageCountMoreThan"
          countNumber = var.image_limit
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}