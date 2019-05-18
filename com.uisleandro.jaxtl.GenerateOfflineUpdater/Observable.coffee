class Observable
  constructor:(obj)->
    obj.ob = []
    obj.observe = @.observe
    obj.tell = @.tell
    obj.addObserver = @.addObserver

  observe:(source)->
    source.addObserver @

  tell:(a)->
    @ob.forEach (x)-> x.update(a)
    return

  addObserver:(b)->
    @ob.push b
    return

# TODO: i need to make some update in the classes which uses this functionality
#  update:($self, args)->
#    $self.run(args)

module.exports = Observable