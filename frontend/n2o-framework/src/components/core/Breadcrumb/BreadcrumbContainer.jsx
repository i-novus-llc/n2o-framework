import React from 'react'
import PropTypes from 'prop-types'
import { ReactReduxContext } from 'react-redux'
import { compose, getContext, withProps } from 'recompose'
import map from 'lodash/map'

import { getModelSelector } from '../../../ducks/models/selectors'
import propsResolver from '../../../utils/propsResolver'

/**
 * Контейнер для {@link DefaultBreadcrumb}
 * @reactProps {object} items -массив из {label, href} объектов, описывающих одну вкладку ("крошку")
 *
 * @example
 *  const items = [
 *    {
 *      href: '/test1',
 *      label: 'test1'
 *    },
 *    {
 *      href: '/test2',
 *      label: 'test2'
 *    },
 *  ]
 * <Breadcrumb  items={items}/>
 * */
function BreadcrumbContainer({ items, defaultBreadcrumb: DefaultBreadcrumb }) {
    return (
        <>
            <DefaultBreadcrumb items={items} />
        </>
    )
}

BreadcrumbContainer.propTypes = {
    defaultBreadcrumb: PropTypes.oneOfType([PropTypes.func, PropTypes.node]),
    items: PropTypes.arrayOf(
        PropTypes.shape({
            label: PropTypes.string,
            modelLink: PropTypes.string,
            path: PropTypes.string,
        }),
    ),
}

BreadcrumbContainer.defaultProps = {
    items: [],
    defaultBreadcrumb: () => null,
}

const ComposedComponent = compose(
    getContext({
        defaultBreadcrumb: PropTypes.oneOfType([PropTypes.func, PropTypes.node]),
    }),
    withProps(props => ({
        items: map(props.items, (item) => {
            if (item.modelLink) {
                return {
                    ...item,
                    label: propsResolver(
                        item.label,
                        getModelSelector(item.modelLink)(props.store.getState()),
                    ),
                }
            }

            return item
        }),
    })),
)(BreadcrumbContainer)

export default props => (
    <ReactReduxContext.Consumer>
        {({ store }) => <ComposedComponent {...props} store={store} />}
    </ReactReduxContext.Consumer>
)
