import { map } from 'lodash';

export const mapToNumOrStr = (count, callback) =>
  map(new Array(parseInt(count)), callback);
