import map from 'lodash/map'

export const mapToNumOrStr = (count, callback) => map(new Array(parseInt(count)), callback)
