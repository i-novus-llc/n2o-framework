import SwitchCell from './SwitchCell'

export const textCellProps = {
    src: 'TextCell',
    fieldKey: 'text',
    text: 'TextCell text',
}

export const linkCellProps = {
    id: 'name',
    src: 'LinkCell',
    fieldKey: 'name',
    className: 'n2o',
    label: 'LinkCell Link',
    style: {},
    icon: 'icon',
    type: 'text',
    url: '/page/widget/test',
    target: 'application',
}

export const imageCellProps = {
    src: 'ImageCell',
    id: 'url',
    className: 'n2o',
    style: {
        color: 'red',
    },
    title: 'default cell',
    shape: 'circle',
}

export const model = {
    src: 'SwitchCell',
    switchFieldId: 'TextCell', // поле в модели, в котором лежит ключ нужного cell
    switchList: {
    // список возможных ячеек
        TextCell: {
            ...textCellProps,
        },
        LinkCell: {
            ...linkCellProps,
        },
    },
    switchDefault: {
        ...imageCellProps,
    },
    model: {
        TextCell: 'TextCell',
    },
}

export const model2 = {
    src: 'SwitchCell',
    switchFieldId: 'LinkCell', // поле в модели, в котором лежит ключ нужного cell
    switchList: {
    // список возможных ячеек
        TextCell: {
            ...textCellProps,
        },
        LinkCell: {
            ...linkCellProps,
        },
    },
    switchDefault: {
        ...imageCellProps,
    },
    model: {
        LinkCell: 'LinkCell',
    },
}

export const modelFromDefaultView = {
    src: 'SwitchCell',
    switchFieldId: 'TextCell', // поле в модели, в котором лежит ключ нужного cell
    switchList: {
    // список возможных ячеек
        anotherKey1: {
            ...textCellProps,
        },
        anotherKey2: {
            ...linkCellProps,
        },
    },
    switchDefault: {
        ...imageCellProps,
    },
    model: {
        TextCell: 'ImageCell',
    },
}
