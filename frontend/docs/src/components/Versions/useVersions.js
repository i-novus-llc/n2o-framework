import { useEffect, useState } from 'react'

export const CONFIG = require('../../ci-config.json')

import { REPLACE_SYMBOL, MAX_VERSIONS, MINOR, TARGET } from './constants'

export function useVersions() {
    const [versions, setVersions] = useState([])
    const [latestVersion, setLatestVersion] = useState(null)

    function parseVersion(version) {
        const converted = String(version)

        const replaced = converted.replace(REPLACE_SYMBOL, '')
        const [major, minor] = replaced.split('.')

        return {
            major: Number(major),
            minor: Number(minor),
            version: Number(`${major}.${minor}`),
        }
    }

    useEffect(() => {
        const fetchVersion = async () => {
            try {
                const response = await fetch(CONFIG.n2oVersionUrl)
                const latestVersion = await response.text()

                const { version } = parseVersion(latestVersion)
                setLatestVersion(version)
            } catch (e) {
                console.error(e)
            }
        }

        fetchVersion().catch(console.error)
    }, [])

    /* FIXME нужно доработать,
        сломается при повышении major когда minor станет 0
         frontend не знает последний патч prev major версии */
    useEffect(() => {
        if (!latestVersion) {
            return
        }

        const { major, minor } = parseVersion(latestVersion)

        const versions = []

        for (let i = MINOR; i <= MAX_VERSIONS; i += MINOR) {
            const previousMinor = minor - i

            versions.push({
                label: `${major}.${previousMinor}`,
                onClick() {
                    const href = `https://${major}-${previousMinor}.n2oapp.net`
                    const { pathname, hash } = window.location

                    window.open(`${href}${pathname}${hash}`, TARGET)
                }
            })
        }
        setVersions(versions)
    }, [latestVersion])

    return {
        versions,
    }
}
