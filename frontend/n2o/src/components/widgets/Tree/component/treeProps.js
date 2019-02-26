import PropTypes from 'prop-types';

export const defaultProps = {
  disabled: false,
  loading: false,
  parentFieldId: 'parentId',
  valueFieldId: 'id',
  labelFieldId: 'label',
  iconFieldId: 'icon',
  imageFieldId: 'image',
  badgeFieldId: 'badge',
  badgeColorFieldId: 'color',
  hasCheckboxes: false,
  datasource: [],
  parentIcon: '',
  childIcon: '',

  checkable: true,
  draggable: false,
  multiple: false,
  prefixCls: 'n2o-tree-widget',
  icon: '',
  selectable: true,
  showLine: true,
  filter: '',
  expandBtn: true
};

export const propTypes = {
  disabled: PropTypes.bool,
  loading: PropTypes.bool,
  parentFieldId: PropTypes.string,
  valueFieldId: PropTypes.string,
  labelFieldId: PropTypes.string,
  iconFieldId: PropTypes.string,
  imageFieldId: PropTypes.string,
  badgeFieldId: PropTypes.string,
  badgeColorFieldId: PropTypes.string,
  hasCheckboxes: PropTypes.bool,
  datasource: PropTypes.array,
  parentIcon: PropTypes.string,
  childIcon: PropTypes.string,

  checkable: PropTypes.bool,
  draggable: PropTypes.bool,
  multiple: PropTypes.bool,
  prefixCls: PropTypes.string,
  icon: PropTypes.string,
  selectable: PropTypes.bool,
  showLine: PropTypes.bool,
  filter: PropTypes.string,
  expandBtn: PropTypes.bool
};

export const TREE_PROPS = [
  'loading',
  'disabled',
  'checkable',
  'draggable',
  'multiple',
  'selectable',
  'showLine'
];

export const TREE_NODE_PROPS = [
  'labelFieldId',
  'iconFieldId',
  'imageFieldId',
  'badgeFieldId',
  'badgeColorFieldId',
  'childIcon',
  'parentIcon',
  'valueFieldId',
  'parentFieldId'
];
