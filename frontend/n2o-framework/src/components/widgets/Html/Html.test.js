import React from 'react';
import { shallow, mount, render } from 'enzyme';
import Html from './Html.jsx';

describe('<Html/>', () => {
  it('should render html 1', () => {
    const wrapper = shallow(<Html html={'<h1>test1</h1>'} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('should render html 2', () => {
    const wrapper = shallow(<Html html={'<h1>test2</h1>'} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('should render html 3 without data', () => {
    const wrapper = shallow(
      <Html html={'<h1>test3</h1>'} resolvePlaceholders={true} />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('should render a html with data and resolvePlaceholders = true', () => {
    const wrapper = shallow(
      <Html
        html={'<h1>name is :name , surname id :surname</h1>'}
        resolvePlaceholders={true}
        data={{ name: 'Tom', surname: 'Sower' }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('should render a html with data and resolvePlaceholders = false', () => {
    const wrapper = shallow(
      <Html
        html={'<h1>name is :name , surname id :surname</h1>'}
        resolvePlaceholders={false}
        data={{ name: 'Tom', surname: 'Sower' }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });
});

it('correct class in html', () => {
  const wrapper = render(
    <Html html={'<h1 class="some">name is :name , surname id :surname</h1>'}
    />
  );
  expect(wrapper.find('.some')).toHaveLength(1);
});

it('correct string in html (resolve placeholders from data) resolvePlaceholders = true', () => {
  const wrapper = render(
    <Html
      html={'name is :name , surname is :surname'}
      resolvePlaceholders={true}
      data={{ name: 'Tom', surname: 'Sower' }}
    />
  );
  expect(wrapper.text()).toBe('name is Tom , surname is Sower');
});

it('correct string in html (resolve placeholders from data) resolvePlaceholders = false', () => {
  const wrapper = render(
    <Html
      html={'name is :name , surname is :surname'}
      resolvePlaceholders={false}
      data={{ name: 'Tom', surname: 'Sower' }}
    />
  );
  expect(wrapper.text()).toBe('name is :name , surname is :surname');
});
