import React from 'react'
import { Link } from 'react-router-dom'
import map from 'lodash/map'
import classNames from 'classnames'
import { Breadcrumb, BreadcrumbItem } from 'reactstrap'
import PropTypes from 'prop-types'

function DefaultBreadcrumb({ items }) {
    const { N2O_ELEMENT_VISIBILITY } = window
    let style = {}

    if (N2O_ELEMENT_VISIBILITY && N2O_ELEMENT_VISIBILITY.breadcrumb === false) {
        style = { ...style, display: 'none' }
    }

    const crumbs = map(items, ({ label, path }, index) => {
        const lastCrumb = index === items.length - 1

        return (
            <BreadcrumbItem key={index} active={lastCrumb}>
                {path && !lastCrumb ? (
                    <Link className="n2o-breadcrumb-link" to={path} key={index}>
                        {label}
                    </Link>
                ) : (
                    <span
                        className={classNames({
                            'n2o-breadcrumb-link n2o-breadcrumb-link_active': lastCrumb,
                            'n2o-breadcrumb-link n2o-breadcrumb-link_disabled': !lastCrumb,
                        })}
                        key={index}
                    >
                        {label}
                    </span>
                )}
            </BreadcrumbItem>
        )
    })

    return <Breadcrumb style={style}>{crumbs}</Breadcrumb>
}

DefaultBreadcrumb.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        label: PropTypes.string,
        modelLink: PropTypes.string,
        path: PropTypes.string,
    })),
}

DefaultBreadcrumb.defaultProps = {
    items: [],
}

export default DefaultBreadcrumb
