import _, {isArray} from 'lodash';
import stubQuery from './stubQuery.json';
import fetchMock from "fetch-mock";
import queryString from 'query-string';
export const parseUrl = (rawUrl) => {
  const parsedUrl = queryString.parseUrl(rawUrl);
  return parsedUrl.query;
};

export default function getStubData(url, options) {
  options = _.isEmpty(options) ? stubQuery : options;
  const params = parseUrl(url);
  const page = parseInt(params.page) - 1;
  const size = parseInt(params.size);
  let sorting = null;
  let sortDir = null;
  let filterKey = null;
  let filterValue = null;
  let count = null;
  _.each(params, (val, key) => {
    const words = key.split('.');
    if (words[0] === 'sorting') {
      sorting = words[1];
      sortDir = val;
    } else if (words[0] === 'filter') {
      filterKey = words[1];
      filterValue = decodeURI(val);
    }
  });
  let list = options.list;

  list = (filterKey && filterValue) ? list.filter(item => isArray(item) ? item[filterKey].indexOf(filterValue) + 1 : item[filterKey] == filterValue) : list;
  count = (filterKey) ? list.length : options.count;
  list = (sorting) ? sortDir === 'DESC' ? _.reverse(_.sortBy(list, sorting)) : _.sortBy(list, sorting) : list;
  return {
    size: options.size || count,
    message: options.message,
    count: count,
    list: list.slice((page*(size || count)), (page*(size || count) + (size || count))),
    page: parseInt(params.page),
    filterValue
  };
}

export const mockToStory = ({ url, timeout }, getData = getStubData) => (props, fn) => () => {
  const delay = (timeout) => new Promise(res => setTimeout(res, timeout));

  fetchMock
    .restore()
    .get(url, async (url) => {
      timeout && await delay(timeout);
      return getData(url, props);
    });

  return fn(props);
};