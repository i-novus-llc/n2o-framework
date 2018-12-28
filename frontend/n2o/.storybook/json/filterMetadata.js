import _ from 'lodash';

export default function filterMetadata(json, keys) {
  return _.pick(json, keys);
}

const excludeMetadata = (json, keys) => _.omit(json, keys);
export { excludeMetadata }