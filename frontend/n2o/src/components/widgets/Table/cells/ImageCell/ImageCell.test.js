import React from 'react';
import { shallow } from 'enzyme';

import ImageCell from './ImageCell';
import imageShapes from './imageShapes';
import { object, text } from '@storybook/addon-knobs/react';

const props = {
  id: 'url',
  model: {
    url:
      'https://beebom-redkapmedia.netdna-ssl.com/wp-content/uploads/2016/01/Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg'
  },
  className: text('className', 'testtest'),
  style: object('style', {
    color: 'red'
  }),
  title: 'top title',
  shape: imageShapes.THUMBNAIL
};

describe('<ImageCell />', () => {
  it('проверяет создание тайтла', () => {
    const wrapper = shallow(<ImageCell {...props} />);

    expect(
      wrapper
        .find('div')
        .first()
        .prop('title')
    ).toEqual(props.title);
  });

  it('проверяет путь до картинки', () => {
    const wrapper = shallow(<ImageCell {...props} />);

    expect(wrapper.children().getElements()[0].props.src).toEqual(props.model[props.id]);
  });

  it('проверяет форму изображения', () => {
    const wrapper = shallow(<ImageCell {...props} />);
    expect(wrapper.children().getElements()[0].props.className).toEqual('img-thumbnail');
  });

  it('проверяет задание класса', () => {
    const wrapper = shallow(<ImageCell {...props} />);

    expect(wrapper.find(`.${props.className}`).exists()).toBeTruthy();
  });

  it('проверяет стили для элемента', () => {
    const wrapper = shallow(<ImageCell {...props} />);

    expect(
      wrapper
        .find(`.${props.className}`)
        .getElements()
        .pop().props.style
    ).toEqual(props.style);
  });
});
