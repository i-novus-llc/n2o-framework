import transform from 'lodash/transform';

export default function mapProp(prop) {
  return transform(
    prop,
    function(result, value, key) {
      result.push({ value: key, label: value });
    },
    []
  );
}
