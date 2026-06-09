import { getGoalImageUrl, getUserAvatarUrl, GOAL_DEFAULT_IMAGE, USER_DEFAULT_AVATAR } from './image.utils';

describe('image utils', () => {
  it('should return default goal image when picture is missing', () => {
    expect(getGoalImageUrl(null)).toBe(GOAL_DEFAULT_IMAGE);
  });

  it('should return provided goal picture', () => {
    expect(getGoalImageUrl('assets/test.png')).toBe('/assets/test.png');
  });

  it('should return default user avatar when picture is missing', () => {
    expect(getUserAvatarUrl(null)).toBe(USER_DEFAULT_AVATAR);
  });
});
