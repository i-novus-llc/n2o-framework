import PropTypes from 'prop-types'

const positions = ['left', 'right']
const shapes = ['square', 'circle', 'rounded']

export const BadgePropTypes = {
    badge: PropTypes.oneOf(['number', 'string']),
    badgeColor: PropTypes.string,
    badgePosition: PropTypes.oneOf(positions),
    badgeShape: PropTypes.oneOf(shapes),
    image: PropTypes.string,
    imagePosition: PropTypes.oneOf(positions),
    imageShape: PropTypes.oneOf(shapes),
    hasMargin: PropTypes.bool,
    className: PropTypes.string,
}
