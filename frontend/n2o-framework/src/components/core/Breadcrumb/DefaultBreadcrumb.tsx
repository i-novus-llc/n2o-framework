import React, { ReactElement, useMemo } from 'react'
import { Breadcrumb, BreadcrumbItem, BreadcrumbProps } from 'reactstrap'
import classNames from 'classnames'
import { useSelector } from 'react-redux'

// @ts-ignore ignore import error from js file
import { breadcrumbsSelector } from '../../../ducks/global/store'
import { ElementVisibility, WindowType } from '../WindowType'

import { breadcrumb } from './const'
import { Crumb } from './Crumb'

export function SimpleBreadcrumb({ items = [] }: { items: breadcrumb }): ReactElement<BreadcrumbProps> {
    const routes: Record<string, string> = useSelector(breadcrumbsSelector)

    return (
        <Breadcrumb className="n2o-breadcrumb">
            {
                items.map(({ label, path }) => {
                    const disabled = !path
                    const pathName = path && routes[path] ? `${path}${routes[path]}` : path

                    return (
                        <BreadcrumbItem className={classNames({ disabled })}>
                            <Crumb label={label} path={pathName} />
                        </BreadcrumbItem>
                    )
                })
            }
        </Breadcrumb>
    )
}

export function DefaultBreadcrumb({ items = [] }: { items: breadcrumb }): ReactElement<BreadcrumbProps> | null {
    const { N2O_ELEMENT_VISIBILITY = { breadcrumb: true } as ElementVisibility } = window as WindowType

    const isBreadcrumbHidden = useMemo(
        () => (!items.length || !N2O_ELEMENT_VISIBILITY.breadcrumb),
        [N2O_ELEMENT_VISIBILITY.breadcrumb, items.length],
    )

    return isBreadcrumbHidden ? null : <SimpleBreadcrumb items={items} />
}

export default DefaultBreadcrumb
