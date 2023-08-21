import React, { ReactElement } from 'react'
import { Link } from 'react-router-dom'

import { Crumb as CrumbProps } from './const'

export function Crumb({
    label,
    path,
}: CrumbProps): ReactElement | null {
    if (!label) {
        return null
    }

    if (!path) {
        return <span>{label}</span>
    }

    return <Link className="n2o-breadcrumb-link" to={path}>{label}</Link>
}
