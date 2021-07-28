import PropTypes from 'prop-types'

const factoryConfigShape = PropTypes.shape({
    controls: PropTypes.objectOf(PropTypes.func),
    widgets: PropTypes.objectOf(PropTypes.func),
    regions: PropTypes.objectOf(PropTypes.func),
    layouts: PropTypes.objectOf(PropTypes.func),
    cells: PropTypes.objectOf(PropTypes.func),
    headers: PropTypes.objectOf(PropTypes.func),
    fieldsets: PropTypes.objectOf(PropTypes.func),
    fields: PropTypes.objectOf(PropTypes.func),
    snippets: PropTypes.objectOf(PropTypes.func),
    templates: PropTypes.objectOf(PropTypes.func),
    buttons: PropTypes.objectOf(PropTypes.element),
})

export default factoryConfigShape
