import PropTypes from 'prop-types'

const menuElementsTypes = {
    id: PropTypes.string,
    title: PropTypes.string,
    content: PropTypes.arrayOf(PropTypes.object),
}

const menuTypes = {
    ...menuElementsTypes,
    menu: menuElementsTypes,
}

export const ScrollSpyTypes = {
    src: PropTypes.string,
    id: PropTypes.string,
    placement: PropTypes.string,
    title: PropTypes.string,
    active: PropTypes.bool,
    className: PropTypes.string,
    headlines: PropTypes.bool,
    style: PropTypes.object,
    menu: menuTypes,
}
