import PropTypes from 'prop-types'

export const propTypes = {
    multiple: PropTypes.bool,
    showTooltip: PropTypes.bool,
    tooltipPlacement: PropTypes.string,
    step: PropTypes.number,
    vertical: PropTypes.bool,
    disabled: PropTypes.bool,
    dots: PropTypes.bool,
    min: PropTypes.number,
    max: PropTypes.number,
    marks: PropTypes.object,
    pushable: PropTypes.bool,
    tooltipFormatter: PropTypes.string,
    stringMode: PropTypes.bool,
}

export const defaultProps = {
    multiple: false,
    showTooltip: false,
    tooltipPlacement: 'top',
    stringMode: true,
    onChange: () => {},
}
