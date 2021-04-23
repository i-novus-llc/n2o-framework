import NotFoundFactory from './NotFoundFactory'

const actionResolver = (src, { actions = {} }) => actions[src] || NotFoundFactory

export default actionResolver
