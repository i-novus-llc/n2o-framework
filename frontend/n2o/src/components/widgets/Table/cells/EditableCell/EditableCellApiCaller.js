import request from '../../../../../utils/request';
import { API_PREFIX, BASE_PATH_DATA } from '../../../../../core/api';

export function editableCellApiCaller(
  options,
  model,
  settings = { apiPrefix: API_PREFIX, basePath: BASE_PATH_DATA }
) {
  return request([settings.apiPrefix, settings.basePath].join(''), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(options),
  });
}
