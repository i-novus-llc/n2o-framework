import React from 'react';
import sinon from 'sinon';
import { NavbarBrand } from 'reactstrap';

import SimpleHeaderContainer from './SimpleHeaderContainer';

const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(
    <SimpleHeaderContainer activeId="link" headerId="test1" />
  );

  return {
    props,
    wrapper,
  };
};

const metadata = {
  items: [
    {
      id: 'link',
      label: 'link',
      href: '/link',
      type: 'link',
      linkType: 'inner',
    },
    {
      id: 'dropdown',
      label: 'dropdown',
      type: 'dropdown',
      subItems: [
        { id: 'link1', label: 'link1', href: 'link1.ru', linkType: 'outer' },
      ],
    },
  ],
  extraItems: [
    {
      id: 'text',
      label: 'text',
      type: 'text',
    },
  ],
  brand: 'N2O',
  search: true,
  brandImage: '/image.svg',
};

describe('<SimpleHeaderContainer />', () => {
  afterEach(function() {
    SimpleHeaderContainer.prototype.fetch.restore();
  });

  it('проверяет что метадаты идет в стейт', done => {
    const p = Promise.resolve(metadata);
    sinon.stub(SimpleHeaderContainer.prototype, 'fetch').callsFake(() => p);
    const { wrapper } = setup();
    p.then(metadata => {
      wrapper.update();
      expect(wrapper.state('metadata')).toEqual(metadata);
      done();
    });
  });
  it('проверяет наличие элементов из метадаты в представлении', done => {
    const p = Promise.resolve(metadata);
    sinon.stub(SimpleHeaderContainer.prototype, 'fetch').callsFake(() => p);
    const { wrapper } = setup();
    p.then(metadata => {
      wrapper.update();
      expect(wrapper.find('a[children="link"]')).toHaveLength(1);
      expect(wrapper.find(NavbarBrand)).toHaveLength(1);
      done();
    });
  });
  it('проверяет, что активный элемент устанавливается правильно', done => {
    const p = Promise.resolve(metadata);
    sinon.stub(SimpleHeaderContainer.prototype, 'fetch').callsFake(() => p);
    const { wrapper } = setup();
    p.then(metadata => {
      wrapper.update();
      expect(wrapper.find('li.active a[children="link"]')).toHaveLength(1);
      done();
    });
  });
});
