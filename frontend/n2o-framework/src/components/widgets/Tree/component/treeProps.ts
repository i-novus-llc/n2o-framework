export const defaultProps = {
    disabled: false,
    loading: false,
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'label',
    iconFieldId: 'icon',
    badge: {
        fieldId: 'badge',
        colorFieldId: 'color',
    },
    hasCheckboxes: false,
    datasource: [],
    parentIcon: '',
    childIcon: '',
    draggable: true,
    multiselect: true,
    prefixCls: 'n2o-rc-tree',
    icon: '',
    selectable: true,
    showLine: false,
    filter: '',
    expandBtn: false,
    onResolve: () => {},
    onDrop: () => {},
    filterPlaceholder: 'Поиск...',
}

export const TREE_PROPS = [
    'loading',
    'disabled',
    'selectable',
    'showLine',
    'prefixCls',
]

export const TREE_NODE_PROPS = [
    'labelFieldId',
    'iconFieldId',
    'imageFieldId',
    'badge',
    'childIcon',
    'parentIcon',
    'valueFieldId',
    'parentFieldId',
    'prefixCls',
    'filter',
]