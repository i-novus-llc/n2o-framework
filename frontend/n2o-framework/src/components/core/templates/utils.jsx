import PropTypes from 'prop-types'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'

export const layoutContainerClasses = (header, sidebar, fullSizeHeader, fixed, side) => classNames(
    'n2o-layout-container flex-grow-1',
    { 'n2o-layout-with-header': !isEmpty(header),
        'n2o-layout-with-sidebar': !isEmpty(sidebar),
        'n2o-layout-full-size-header d-flex flex-column': fullSizeHeader,
        'n2o-layout-full-size-sidebar d-flex': !fullSizeHeader,
        'n2o-layout-fixed': fixed,
        'flex-row-reverse': !fullSizeHeader && side === 'right',
    },
)

export const layoutTypes = {
    layoutClassName: PropTypes.string,
    side: PropTypes.string,
    header: PropTypes.object,
    sidebar: PropTypes.object,
    footer: PropTypes.object,
    rest: PropTypes.object,
    toggleSidebar: PropTypes.func,
    sidebarOpen: PropTypes.bool,
    fixed: PropTypes.bool,
    layoutChildren: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
}
