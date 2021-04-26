import React from 'react'
import { Link } from 'react-router-dom'
import map from 'lodash/map'
import cn from 'classnames'
import Breadcrumb from 'reactstrap/lib/Breadcrumb'
import BreadcrumbItem from 'reactstrap/lib/BreadcrumbItem'

function DefaultBreadcrumb({ items }) {
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
                        className={cn({
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

    return <Breadcrumb>{crumbs}</Breadcrumb>
}

export default DefaultBreadcrumb
