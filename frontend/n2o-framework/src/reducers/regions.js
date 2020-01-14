import {
  REGISTER_REGION,
  SET_ACTIVE_REGION_ENTITY,
} from '../constants/regions';

export default function regions(state = {}, action) {
  const { type, payload } = action;

  switch (type) {
    case REGISTER_REGION:
      return Object.assign({}, state, {
        [payload.regionId]: payload.initProps,
      });
    case SET_ACTIVE_REGION_ENTITY:
      return Object.assign({}, state, {
        [payload.regionId]: Object.assign({}, state[payload.regionId], {
          activeEntity: payload.activeEntity,
        }),
      });
    default:
      return state;
  }
}
