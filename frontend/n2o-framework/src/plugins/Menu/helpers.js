import isEmpty from 'lodash/isEmpty'
import pathToRegexp from 'path-to-regexp'

const asteriskCount = str => str.split('*').length - 1

export const getMatchingSidebar = (sidebars, pathname) => [...sidebars]
    .map(sidebar => ({ ...sidebar, path: sidebar.path ? sidebar.path.replace('*', '(.*)') : '(.*)' }))
    .filter(sidebar => !isEmpty(pathToRegexp(sidebar.path).exec(pathname)))
    .sort((a, b) => asteriskCount(a.path) - asteriskCount(b.path))[0]
