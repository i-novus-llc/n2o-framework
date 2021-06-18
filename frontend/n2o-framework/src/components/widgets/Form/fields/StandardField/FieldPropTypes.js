import PropTypes from 'prop-types'

export const ControlPropTypes = PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.object,
]).isRequired

export const FieldActionsPropTypes = PropTypes.arrayOf(
    PropTypes.shape({
        label: PropTypes.string.isRequired,
    }),
)
