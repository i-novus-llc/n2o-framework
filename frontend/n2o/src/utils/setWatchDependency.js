import { get, map, reduce, set } from 'lodash';

const pickByPath = (object, arrayToPath) =>
  reduce(arrayToPath, (o, p) => set(o, p, get(object, p)), {});

const DEPENDENCY_TYPES = {
  RE_RENDER: 'reRender'
};

export default (state, props) => {
  const { dependency, form } = props;

  const pickByReRender = (acc, { type, on }) => {
    if (on && type === DEPENDENCY_TYPES.RE_RENDER) {
      const formOn = map(on, item => ['form', form, 'values', on].join('.'));
      return { ...acc, ...pickByPath(state, formOn) };
    }
  };
  return reduce(dependency, pickByReRender, {});
};
