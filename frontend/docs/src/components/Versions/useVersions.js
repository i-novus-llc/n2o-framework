import { useEffect, useState } from 'react'

export const CONFIG = require('../../ci-config.json')

const SANDBOX_PATH = '/sandbox'
const FULL_VERSION_PATH = `/${SANDBOX_PATH}/n2o/version`
const TARGET = '_self'

export function useVersions() {
    const [versions, setVersions] = useState([])
    const { sandboxStandsUrl } = CONFIG

    function open(href) {
        const { pathname, hash } = window.location
        window.open(`${href}${pathname}${hash}`, TARGET)
    }

    useEffect(() => {
        let isMounted = true

        const fetchAllVersions = async () => {
            try {
                const standsResponse = await fetch(sandboxStandsUrl)
                if (!standsResponse.ok) {
                    throw new Error(`Failed to load stands: ${standsResponse.status}`)
                }

                const stands = await standsResponse.json()

                const promises = stands.map(async (stand) => {
                    const baseUrl = stand.url.replace(/\/$/, '')
                    const versionUrl = `${baseUrl}${FULL_VERSION_PATH}`

                    try {
                        const versionResponse = await fetch(versionUrl)
                        if (!versionResponse.ok) {
                            throw new Error(`Version request failed: ${versionResponse.status}`)
                        }

                        return {
                            label: await versionResponse.text(),
                            onClick: () => open(baseUrl)
                        }
                    } catch (error) {
                        console.error(`Failed to fetch version from ${versionUrl}:`, error)
                        return {
                            label: stand.name,
                            access: false
                        }
                    }
                })

                const results = await Promise.all(promises)
                if (isMounted) setVersions(results)
            } catch (error) {
                console.error('Failed to load stands:', error)

                if (isMounted) setVersions([])
            }
        }

        fetchAllVersions().catch(error => console.error(error))

        return () => { isMounted = false }
    }, [sandboxStandsUrl])

    return { versions }
}
