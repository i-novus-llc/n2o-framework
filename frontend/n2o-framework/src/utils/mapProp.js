import { transform } from 'lodash';

export default function mapProp(prop) {
  return transform(
    prop,
    function(result, value, key) {
      result.push({ value: key, label: value });
    },
    []
  );
}
