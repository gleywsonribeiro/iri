SELECT CASE p.qualificador 
         WHEN 'PT' THEN 'Escala de Tomada de Perspectiva' 
         WHEN 'FS' THEN 'Escala de Fantasia' 
         WHEN 'EC' THEN 'Escala de Consideração Empática' 
         WHEN 'PD' THEN 'Escala de Angústia Pessoal' 
		 else 'Escala de Fantasia'
       END escala, 
       Sum(o.peso) 
FROM   avaliacao av, 
       pesquisa pes, 
       resposta r, 
       pergunta p, 
       opcao o 
WHERE  pes.id = av.pesquisa_id 
       AND r.avaliacao_id = av.id 
       AND r.pergunta_id = p.id 
       AND r.opcao_id = o.id 
	   AND p.tipo = 'AUTOMATICO'
       AND av.id IN (SELECT av.id 
                     FROM   avaliacao av, 
                            pesquisa pes, 
                            resposta r, 
                            pergunta p, 
                            opcao o 
                     WHERE  pes.id = av.pesquisa_id 
                            AND r.avaliacao_id = av.id 
                            AND r.pergunta_id = p.id 
                            AND r.opcao_id = o.id 
                            --AND o.id = 1
                    ) 
       
GROUP  BY qualificador
-- http://www.dpriver.com/pp/sqlformat.htm

select av.id, av.datahora, r.id, perg.descricao, op.descricao
from avaliacao av, resposta r, pergunta perg, opcao op
where r.avaliacao_id = av.id
and av.id = 1036
and r.pergunta_id = perg.id
and r.opcao_id = op.id
order by av.id, perg.descricao, op.descricao


select * from resposta where id = 1073