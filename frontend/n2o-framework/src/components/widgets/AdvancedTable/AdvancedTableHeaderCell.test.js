import React from 'react';
import AdvancedTableHeaderCell from './AdvancedTableHeaderCell';

const Classes = {
  HEADER: '.n2o-advanced-table-header-cel',
  CONTENT: '.n2o-advanced-table-header-cell-content',
  STRING_CHILDREN: '',
};

const setup = propsOverride => {
  const props = {
    id: 'name',
  };

  return mount(<AdvancedTableHeaderCell {...props} {...propsOverride} />);
};

describe('<AdvancedTableHeaderCell>', () => {
  it('компонент отрисовывается', () => {
    const wrapper = setup({
      title: 'test',
    });

    expect(wrapper.find(Classes.HEADER).exists()).toBe(true);
    expect(wrapper.find(Classes.CONTENT).text()).toBe('test');
  });

  it('отрисовывается по текстовому children', () => {
    const wrapper = setup({
      children: 'children string',
    });

    expect(wrapper.find(Classes.HEADER).text()).toBe('children string');
  });

  it('отрисовывается multiHeader', () => {
    const wrapper = setup({
      multiHeader: true,
      children: [],
      component: () => <div>Заголовок</div>,
    });

    expect(wrapper.find(Classes.HEADER + ' div').text()).toBe('Заголовок');
  });

  it('отрисовывается children компонент', () => {
    const wrapper = setup({
      children: <div className="custom-component">Test</div>,
    });

    expect(wrapper.find('.custom-component').exists()).toBe(true);
    expect(wrapper.find('.custom-component').text()).toBe('Test');
  });

  it('отрисовывается с иконкой', () => {
    const wrapper = setup({
      title: 'test',
      icon: 'fa fa-plus',
    });

    expect(wrapper.find('i.fa-plus').exists()).toBe(true);
  });

  it('отрисовывается с фильтрацией', () => {
    const wrapper = setup({
      filterable: true,
      title: 'test',
    });

    expect(wrapper.find('AdvancedTableFilter').exists()).toBe(true);
  });

  it('отрисовывается с resize', () => {
    const wrapper = setup({
      title: 'test',
      width: '100px',
      resizable: true,
    });

    expect(wrapper.find('Resizable').exists()).toBeTruthy();
  });
});
