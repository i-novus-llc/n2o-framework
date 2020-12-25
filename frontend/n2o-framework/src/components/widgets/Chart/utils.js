import omit from 'lodash/omit';

export const parseData = data => {
  if (data.length) {
    return data.map(item => {
      const { hasLabel, label } = item;
      return {
        ...omit(item, ['hasLabel', 'label']),
        label: hasLabel,
        name: label,
      };
    });
  }
};
