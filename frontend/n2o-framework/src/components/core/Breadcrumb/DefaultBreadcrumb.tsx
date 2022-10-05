import React, { ReactElement } from 'react'
import { Breadcrumb, BreadcrumbItem, BreadcrumbProps } from 'reactstrap'
import classNames from 'classnames'

import { breadcrumb } from './const'
import { Crumb } from './Crumb'
// @ts-ignore import from js file
import { globalBreadcrumbVisibility } from './globalBreadcrumbVisibility'

export function DefaultBreadcrumb({
    items = [],
}: { items: breadcrumb }): ReactElement<BreadcrumbProps> | null {
    if (!items.length || globalBreadcrumbVisibility === false) {
        return null
    }

    return (
        <Breadcrumb className="n2o-breadcrumb">
            {
                items.map(({ label, path }, index) => {
                    const disabled = !path

                    return (
                        <BreadcrumbItem className={classNames({ disabled })}>
                            <Crumb label={label} path={path} />
                        </BreadcrumbItem>
                    )
                })
            }
        </Breadcrumb>
    )
}

export default DefaultBreadcrumb
