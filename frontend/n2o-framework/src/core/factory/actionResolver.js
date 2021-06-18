import { NotFoundFactory } from './NotFoundFactory'

export const actionResolver = (src, { actions = {} }) => actions[src] || NotFoundFactory

export default actionResolver
