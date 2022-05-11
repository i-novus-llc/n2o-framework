import isEmpty from 'lodash/isEmpty'
import pathToRegexp from 'path-to-regexp'

const asteriskCount = str => str.split('*').length - 1

export const getMatchingSidebar = (sidebars, pathname) => [...sidebars]
    .map(sidebar => ({ ...sidebar, path: sidebar.path ? sidebar.path.replace(/\*/g, '(.*)') : '/(.*)' }))
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
    .sort((a, b) => (b.path.length - asteriskCount(b.path)) - (a.path.length - asteriskCount(a.path)))[0]
