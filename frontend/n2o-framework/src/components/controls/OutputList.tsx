import React, { useMemo } from 'react'
import classNames from 'classnames'
import get from 'lodash/get'
import { OutputList as List, Props as ListProps } from '@i-novus/n2o-components/lib/display/OutputList'
import { Link } from '@i-novus/n2o-components/lib/navigation/Link'
import { LinkProps } from '@i-novus/n2o-components/lib/navigation/types'

import { N2OLinkTarget } from '../core/router/types'
import { useLink } from '../core/router/useLink'

export type Props = Omit<ListProps, 'LinkComponent' | 'target' | 'value'> & {
    target: N2OLinkTarget
    newWindow?: boolean
    disabled?: boolean
    value?: ListProps['value'] | string
}

const getN2OLink = (target: N2OLinkTarget, newWindow?: boolean, disabled?: boolean) => {
    return function N2OLink({ className, url, ...rest }: LinkProps) {
        const { active, ...linkProps } = useLink({ href: url, disabled, target, newWindow })

        return (
            <Link
                className={classNames(className, { active })}
                {...rest}
                {...linkProps}
            />
        )
    }
}

export function OutputList({
    target,
    newWindow,
    disabled,
    value,
    labelFieldId = 'name',
    ...rest
}: Props) {
    const linkTarget = newWindow ? '_blank' : undefined
    const LinkComponent = useMemo(() => getN2OLink(target, newWindow, disabled), [target, newWindow, disabled])
    // const list = Array.isArray(value) ? value : [value]
    // const outputValue = list.filter(Boolean).map(item => (
    //     typeof item === 'object'
    //         ? item
    //         : { [labelFieldId]: item }
    // ))

    // Костыль для обратной совместимости
    // FIXME выпилить, или как минимум заменить на вариант выше
    const outputValue = typeof value === 'string'
        ? value.split('').map(() => ({ [labelFieldId]: get(rest, labelFieldId) }))
        : value

    return (
        <List
            {...rest}
            LinkComponent={LinkComponent}
            target={linkTarget}
            disabled={disabled}
            labelFieldId={labelFieldId}
            value={outputValue}
        />
    )
}
