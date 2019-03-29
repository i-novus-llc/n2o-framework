import PropTypes from 'prop-types';

export let ControlPropTypes = PropTypes.oneOfType([PropTypes.string, PropTypes.object]).isRequired;

export let FieldActionsPropTypes = PropTypes.arrayOf(
  PropTypes.shape({
    label: PropTypes.string.isRequired
  })
);
