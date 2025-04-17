import React from 'react'
import { Link } from 'react-router-dom'

import { Crumb as Props } from './const'

export function Crumb({ label, path }: Props) {
    if (!label) { return null }

    if (!path) { return <span>{label}</span> }

    return <Link className="n2o-breadcrumb-link" to={path}>{label}</Link>
}
