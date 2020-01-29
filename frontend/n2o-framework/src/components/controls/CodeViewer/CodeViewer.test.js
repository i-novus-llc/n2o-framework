import React from 'react';

import { CodeViewer } from './CodeViewer';

const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(<CodeViewer {...props} />);

  return {
    props,
    wrapper,
  };
};

describe('<CodeViewer />', () => {
  it('виден по visible', () => {
    let { wrapper } = setup({ visible: false });
    expect(wrapper.find('.n2o-code-viewer')).toHaveLength(0);
    wrapper = setup({ visible: true }).wrapper;
    expect(wrapper.find('.n2o-code-viewer')).toHaveLength(1);
  });

  it('SyntaxHighlighter доступен по show', () => {
    let { wrapper } = setup({ show: true });
    expect(wrapper.find('SyntaxHighlighter')).toHaveLength(1);
  });
});