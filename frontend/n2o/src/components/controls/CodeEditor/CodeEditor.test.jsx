import React from 'react';

import AceEditor from 'react-ace';
import CodeEditor from './CodeEditor';

const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = shallow(<CodeEditor {...props} />);

  return {
    props,
    wrapper,
  };
};

describe('<CodeEditor />', () => {
  it('виден по visible', () => {
    let { wrapper } = setup({ visible: false });
    expect(wrapper.find(AceEditor)).toHaveLength(0);
    wrapper = setup({ visible: true }).wrapper;
    expect(wrapper.find(AceEditor)).toHaveLength(1);
  });

  it('доступен по disabled', () => {
    let { wrapper } = setup({ disabled: true });
    expect(wrapper.find(AceEditor).props().readOnly).toBe(true);
  });
});
