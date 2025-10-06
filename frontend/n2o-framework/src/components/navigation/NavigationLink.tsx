import React from 'react'
import get from 'lodash/get'
import { Link } from '@i-novus/n2o-components/lib/navigation/Link'
import { ROOT_CLASS_NAME_PARAM } from '@i-novus/n2o-components/lib/navigation/helpers'
import classNames from 'classnames'

import { useLink } from '../../core/router/useLink'
import { WithDataSource } from '../../core/datasource/WithDataSource'
import { type Model } from '../../ducks/models/selectors'

import { type NavigationLinkProps } from './types'

function Component({
    model: modelPrefix,
    models,
    url,
    visible = true,
    enabled = true,
    pathMapping,
    queryMapping,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
    className,
    ...rest
}: NavigationLinkProps) {
    const model = get(models, modelPrefix, {}) as Model

    const resolvedProps = useLink({
        url,
        pathMapping,
        queryMapping,
        model,
        visible,
        enabled,
    })

    return <Link className={classNames(className, rootClassName)} {...rest} {...resolvedProps} />
}

export const NavigationLink = WithDataSource<NavigationLinkProps>(Component)
