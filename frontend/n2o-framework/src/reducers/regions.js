import {
  REGISTER_REGION,
  SET_ACTIVE_REGION_ENTITY,
} from '../constants/regions';

const resolveActiveEntity = activeEntity => {
  switch (activeEntity) {
    case 'true':
      return true;
    case 'false':
      return false;
    default:
      return activeEntity;
  }
};

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
          activeEntity: resolveActiveEntity(payload.activeEntity),
        }),
      });
    default:
      return state;
  }
}
