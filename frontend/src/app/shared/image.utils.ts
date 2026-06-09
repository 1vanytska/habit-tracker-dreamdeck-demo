export const GOAL_DEFAULT_IMAGE = '/assets/images/placeholder-goal.png';

export const USER_DEFAULT_AVATAR = '/assets/images/user.svg';

function hashString(value: string): number {
  let hash = 0;
  for (let i = 0; i < value.length; i++) {
    hash = (hash << 5) - hash + value.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash);
}

function normalizeAssetPath(path: string): string {
  if (path.startsWith('/assets/')) {
    return path;
  }
  if (path.startsWith('assets/')) {
    return `/${path}`;
  }
  return path;
}

export function getGoalImageUrl(picture: string | null | undefined): string {
  if (picture) {
    return normalizeAssetPath(picture);
  }
  return GOAL_DEFAULT_IMAGE;
}

export function getUserAvatarUrl(picture: string | null | undefined): string {
  if (picture) {
    return normalizeAssetPath(picture);
  }
  return USER_DEFAULT_AVATAR;
}

export function resolveGoalPictureForSave(
  picture: string | null | undefined
): string {
  if (picture?.trim()) {
    return picture.trim();
  }
  return GOAL_DEFAULT_IMAGE;
}

export function resolveUserAvatarForSave(
  picture: string | null | undefined
): string {
  if (picture?.trim()) {
    return picture.trim();
  }
  return USER_DEFAULT_AVATAR;
}
