(define (domain vanilla_world)
  (:requirements :strips)
  (:predicates
   (adj ?square-1 ?square-2)
   (white ?square)
   (at ?what ?square)
   (robot ?who)  
  )

  (:action move
    :parameters (?who ?from ?to)

    :precondition (and (robot ?who)
		       (at ?who ?from)
		       (adj ?from ?to)
		       (white ?to)
		       (white ?from)	
		  )

    :effect (and (not (at ?who ?from))
		 (at ?who ?to)
	    )
  )
)
