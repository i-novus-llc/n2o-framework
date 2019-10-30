import React from 'react';
import { Divider } from './Divider';
import { mount, shallow } from 'enzyme/build';
import renderer from 'react-test-renderer';

const setup = propOverrides => {
  const props = Object.assign(
    {
      children: 'title',
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(<Divider {...props} />);

  return wrapper;
};

describe('<Divider />', () => {
  it('проверяет создание элемента Divider', () => {
    let wrapper = shallow(<Divider />);
    expect(wrapper.find('.divider-h').exists()).toBeTruthy();
    expect(wrapper.find('.divider_horizontal').exists()).toBeTruthy();
  });
  it('проверяет наличие children', () => {
    let wrapper = setup();
    expect(wrapper.find('span').exists()).toBeTruthy();
  });
  it('проверка на соответствие заголовка = title', () => {
    let wrapper = setup();
    expect(wrapper.text()).toBe('title');
  });
  it('renders correctly', () => {
    // eslint-disable-next-line no-undef
    const tree = renderer
      .create(<Divider style={undefined}>title</Divider>)
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});
