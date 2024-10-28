import isEmpty from 'lodash/isEmpty'
import pathToRegexp from 'path-to-regexp'

export const libAsterisk = '(.*)' // this string is used by pathToRegexp library instead of common asterisk
const asteriskLength = (str: string) => (str.split(libAsterisk).length - 1) * libAsterisk.length

interface Sidebar {
    path?: string
}

export const getMatchingSidebar = (sidebars: Sidebar[], pathname: string) => [...sidebars]
    .map(sidebar => ({ ...sidebar, path: sidebar.path ? sidebar.path.replace(/\*/g, libAsterisk) : `/${libAsterisk}` }))
    .filter((sidebar) => {
        const array = pathToRegexp(sidebar.path).exec(pathname) || []
        const isValid = array.every((e, i) => {
            if (i > 0 && e.includes('/') && e.length > 1) {
                return array[i + 1] !== undefined && array[i + 1].length === 0
            }

            return true
        })

        return !isEmpty(array) && isValid
    })
    .sort((a, b) => (b.path.length - asteriskLength(b.path)) - (a.path.length - asteriskLength(a.path)))[0]
