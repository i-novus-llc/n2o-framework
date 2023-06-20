import React from 'react'
import DropdownNavbarItem from '@theme/NavbarItem/DropdownNavbarItem'

import {useVersions} from "./useVersions"

const CONFIG = require('../../ci-config.json')

export function Versions(props) {
    const { versions } = useVersions()

    if (!versions.length) {
        return null
    }

    const { n2oVersion } = CONFIG

    return <DropdownNavbarItem items={versions} label={n2oVersion} {...props} />
}
