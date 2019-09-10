import React from 'react';
import { withWidgetHandlers } from './TreeContainer';
import { setTableSelectedId } from '../../../../actions/widgets';

const props = {
  widgetId: 'props.widgetId',
  pageId: 'props.pageId',
  isActive: 'props.isActive',
  hasFocus: 'props.hasFocus',
  hasSelect: 'props.hasSelect',
  autoFocus: 'props.autoFocus',
  datasource: 'props.datasource',
  resolveModel: 'props.resolveModel',
  onResolve: newModel => {
    props.onResolve(newModel);
    if (props.selectedId != newModel.id) {
      props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
    }
  },
  onFocus: 'props.onFocus',
  size: 'props.size',
  actions: 'props.actions',
  redux: true,
  onActionImpl: 'props.onActionImpl',
  rowClick: 'props.rowClick',
  childIcon: 'props.childIcon',
  multiselect: 'props.multiselect',
  showLine: 'props.showLine',
  filter: 'props.filter',
  expandBtn: 'props.expandBtn',
  bulkData: 'props.bulkData',
  parentFieldId: 'props.parentFieldId',
  valueFieldId: 'props.valueFieldId',
  labelFieldId: 'props.labelFieldId',
  iconFieldId: 'props.iconFieldId',
  imageFieldId: 'props.imageFieldId',
  badgeFieldId: 'props.badgeFieldId',
  badgeColorFieldId: 'props.badgeColorFieldId',
  hasCheckboxes: 'props.hasCheckboxes',
  draggable: 'props.draggable',
  childrenFieldId: 'props.childrenFieldId',
};

const setupComponent = propsOverride => {
  return mount(<withWidgetHandlers {...props} {...propsOverride} />);
};

describe('Тесты TreeContainer', () => {
  it('Отрисовка withWidgetHandlers', () => {
    const wrapper = setupComponent();
    console.log(wrapper.debug());
    expect(wrapper);
  });
});
