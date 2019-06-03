import PropTypes from 'prop-types';

const factoryConfigShape = PropTypes.shape({
  pages: PropTypes.objectOf(PropTypes.element),
  controls: PropTypes.objectOf(PropTypes.element),
  widgets: PropTypes.objectOf(PropTypes.element),
  regions: PropTypes.objectOf(PropTypes.element),
  layouts: PropTypes.objectOf(PropTypes.element),
  cells: PropTypes.objectOf(PropTypes.element),
  headers: PropTypes.objectOf(PropTypes.element),
  fieldsets: PropTypes.objectOf(PropTypes.element),
  fields: PropTypes.objectOf(PropTypes.element),
  snippets: PropTypes.objectOf(PropTypes.element),
});

export default factoryConfigShape;
