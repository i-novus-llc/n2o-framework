import PropTypes from 'prop-types'

export const propTypes = {
    header: PropTypes.element,
    footer: PropTypes.element,
    sidebar: PropTypes.element,
    children: PropTypes.any,
    side: PropTypes.oneOf(['left', 'right']),
    fixed: PropTypes.bool,
}
