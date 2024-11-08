import PropTypes from 'prop-types'

const menuElementsTypes = {
    id: PropTypes.string,
    title: PropTypes.string,
    content: PropTypes.arrayOf(PropTypes.object),
}

const itemsTypes = {
    ...menuElementsTypes,
    menu: menuElementsTypes,
}

// TODO для инфо, удалить после рефакторинга ScrollSpyTypes на ts
export const ScrollSpyTypes = {
    src: PropTypes.string,
    id: PropTypes.string,
    placement: PropTypes.string,
    title: PropTypes.string,
    active: PropTypes.bool,
    disabled: PropTypes.bool,
    className: PropTypes.string,
    headlines: PropTypes.bool,
    style: PropTypes.object,
    items: itemsTypes,
    height: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
}
