export interface Goal {
  goalId: string;
  userId: string;
  title: string;
  picture: string | null;
  description: string;
  categoryId: string;
  isPublic: boolean;
  startDate: string;
  deadline: string;
  status: string;
  isArchived: boolean;
  archivingTime: string | null;
}