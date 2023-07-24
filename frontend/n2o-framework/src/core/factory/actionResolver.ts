import { NotFoundFactory } from './NotFoundFactory'

export type Actions = Record<string, (() => void)>
export const actionResolver = (src: string, { actions = {} }: { actions: Actions }) => actions[src] || NotFoundFactory

export default actionResolver
