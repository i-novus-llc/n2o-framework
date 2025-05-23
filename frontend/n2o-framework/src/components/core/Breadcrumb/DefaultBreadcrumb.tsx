import React, { ReactElement, useMemo } from 'react'
import { Breadcrumb, BreadcrumbItem, BreadcrumbProps } from 'reactstrap'
import classNames from 'classnames'
import { useSelector } from 'react-redux'

import { breadcrumbsSelector } from '../../../ducks/global/selectors'
import { ElementVisibility, WindowType } from '../WindowType'
import { EMPTY_ARRAY } from '../../../utils/emptyTypes'

import { type Breadcrumb as Props } from './const'
import { Crumb } from './Crumb'

export function SimpleBreadcrumb({ items = EMPTY_ARRAY }: { items: Props }): ReactElement<BreadcrumbProps> {
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

export function DefaultBreadcrumb({ items = EMPTY_ARRAY }: { items: Props }): ReactElement<BreadcrumbProps> | null {
    const { N2O_ELEMENT_VISIBILITY = { breadcrumb: true } as ElementVisibility } = window as WindowType

    const isBreadcrumbHidden = useMemo(
        () => (!items.length || !N2O_ELEMENT_VISIBILITY.breadcrumb),
        [N2O_ELEMENT_VISIBILITY.breadcrumb, items.length],
    )

    return isBreadcrumbHidden ? null : <SimpleBreadcrumb items={items} />
}

export default DefaultBreadcrumb
